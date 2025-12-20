import React, { useState, useEffect, useRef } from 'react'
import { useParams } from 'react-router-dom'
import TaskCard from './TaskCard.jsx'
import { createTask, getTasks } from '../../api/todoApi.js';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import useInfinitePagination from '../../utils/useInfinitePagination.js';
import { closestCorners, DndContext, KeyboardSensor, PointerSensor, TouchSensor, useSensor, useSensors } from '@dnd-kit/core';
import { SortableContext, verticalListSortingStrategy, arrayMove, sortableKeyboardCoordinates } from '@dnd-kit/sortable';
import { restrictToParentElement } from '@dnd-kit/modifiers';

function Workspace() {
	const queryClient = useQueryClient();
	const {uuid} = useParams();   // workspace uuid
	const [tasks, setTasks] = useState([]);
	const [newTask, setNewTask] = useState("");

	const {
		data,
		items: tasks_data,
		bottomRef,
		isLoading,
		isError,
	} = useInfinitePagination({
		queryKey: ['tasks'],
		queryFn: (page, size) => getTasks(uuid, page, size),
		size: 10,
		enabled: !!uuid,
	});

	useEffect(() => {
		if(!data) return;
		setTasks(tasks_data);
	},[data]);

	const {
		mutate, 
		isLoading: 
		isCreating, 
		isCreateError
	} = useMutation({
		mutationFn: (data) => createTask(uuid, data),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['tasks']});
		},
	});

	function handleInputChange(event) {
		setNewTask(event.target.value);
	}

	// TODO: instead of modifying the actual state from useInfinitePagination hook, perform delete and move api calls.
	// TODO: use DECIMAL fraction for position/move operations.

	function addTask(){
		// NOTE: object of task will be changing in future so name and description are same right now
		mutate({
			name: newTask.split(' ').join('_'),
			description: newTask,
			completed: false
		});
	}

	function deleteTask(index){

	}

	const handleDragEnd = (event) => {
		const { active, over } = event;
		if (!over || active.id === over.id) return;

		setTasks(prev => {
			const from = prev.findIndex(t => t.uuid === active.id);
			const to   = prev.findIndex(t => t.uuid === over.id);
			if (from === -1 || to === -1) return prev;

			const reordered = arrayMove(prev, from, to);

			queryClient.setQueryData(['tasks'], old => {
			if (!old) return old;

			let cursor = 0;
			const newPages = old.pages.map(page => {
				const slice = reordered.slice(cursor, cursor + page.content.length);
				cursor += page.content.length;
				return { ...page, content: slice };
			});

			return { ...old, pages: newPages };
			});

			return reordered;
		});
	};

	const sensors = useSensors(
		useSensor(PointerSensor),
		useSensor(TouchSensor),
		useSensor(KeyboardSensor, {
			coordinateGetter: sortableKeyboardCoordinates,
		})
	);
	
	return (
		<div className='min-h-dvh bg-gray-800'>
			<div className='flex flex-col items-center p-4 gap-10'>
				<div className='flex py-10'>
					<textarea className='bg-white p-2 rounded-l-sm text-xl resize-none' 
					type="text" name="input_todo" rows="1" placeholder='Enter text here' 
					value={newTask} onChange={handleInputChange}  />
					<button className=' text-xl bg-green-500 hover:bg-green-600 px-4 p-2 rounded-r-sm' 
					onClick={() => addTask()}>Add</button>
				</div>
				<div className='w-full px-4 flex flex-col items-center gap-8'>
					<DndContext sensors={sensors} collisionDetection={closestCorners} modifiers={[restrictToParentElement]} onDragEnd={handleDragEnd}>
						<SortableContext items={tasks.map(task => task.uuid)} strategy={verticalListSortingStrategy}>
							{
								tasks.map((task, index) => 
									<TaskCard 
										key={task.uuid} 
										id={task.uuid}
										text={task.description} 
										index={index}
										onDelete={deleteTask} 
									/>
								)
							}
						</SortableContext>
					</DndContext>
				</div>
				{isLoading && <p className='text-white text-2xl'>Loading...</p>}
				{isError && <p className='text-red-500 text-2xl'>*Error fetching tasks</p>}
				{(tasks.length === 0) && <p className='text-white text-2xl'>No task found, add task using above input box.</p>}
				{/* sentinel element */}
				<div ref={bottomRef}></div>
			</div>
		</div>
	)
}

export default Workspace