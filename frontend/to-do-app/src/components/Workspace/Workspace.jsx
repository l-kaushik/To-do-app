import React, { useState, useEffect, useRef } from 'react'
import { useParams } from 'react-router-dom'
import TaskCard from './TaskCard.jsx'
import { createTask, updateTask, removeTask, getTasks } from '../../api/todoApi.js';
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
	const MIN_RANK = "000000";
	const MAX_RANK = "zzzzzz";

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
			setNewTask('');
		},
	});

	const {
		mutate: update,
		isUpdateError
	} = useMutation({
		mutationFn: (data) => updateTask(uuid, data),
		onSuccess: () => {
			queryClient.invalidateQueries({queryKey: ['tasks']});
		}
	});

	const {
		mutate: deleteTask,
	} = useMutation({
		mutationFn: (taskUuid) => removeTask(uuid, taskUuid),
		onSuccess: () => {
			queryClient.invalidateQueries({queryKey: ['tasks']});
		} 
	})

	function handleInputChange(event) {
		setNewTask(event.target.value);
	}

	// TODO: instead of modifying the actual state from useInfinitePagination hook, perform delete and move api calls.
	// TODO: use DECIMAL fraction for position/move operations.

	function addTask() {
		const trimmed = newTask.trim();
		if (trimmed === "") return;

		mutate({
			description: trimmed,
			completed: false
		});
	}

	const handleUpdateTask = (taskList, activeTaskUuid, isCompleted = null) => {
		const index = taskList.findIndex(t => t.uuid === activeTaskUuid);

		update({
			uuid: activeTaskUuid,
			beforeRank: index === 0 ? MIN_RANK : taskList[index - 1].rank,
			afterRank:
			index === taskList.length - 1
				? MAX_RANK : taskList[index + 1].rank,
			description: taskList[index].description,
			completed: isCompleted ?? taskList[index].completed
		});
	}

	const handleOnCheckboxClicked = (taskUuid, newValue) => {
		handleUpdateTask(tasks, taskUuid, newValue);
	}

	const handleDragEnd = (event) => {
		const { active, over } = event;
		if (!over || active.id === over.id) return;

		const from = tasks.findIndex(t => t.uuid === active.id);
		const to   = tasks.findIndex(t => t.uuid === over.id);
		if (from === -1 || to === -1) return;

		const reordered = arrayMove(tasks, from, to);

		// update api call
		handleUpdateTask(reordered, active.id);

		// update local state
		setTasks(reordered);

		// update infinite query cache
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
										isCompleted={task.completed}
										onChange={handleOnCheckboxClicked}
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