import React, { useState, useEffect, useRef } from 'react'
import { useParams } from 'react-router-dom'
import TaskCard from './TaskCard.jsx'
import { createTask, getTasks } from '../../api/todoApi.js';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import useInfinitePagination from '../../utils/useInfinitePagination.js';

function Workspace() {
	const queryClient = useQueryClient();
	const {uuid} = useParams();   // workspace uuid
	const [newTask, setNewTask] = useState("");

	const {
		items: tasks,
		bottomRef,
		isLoading,
		isError,
	} = useInfinitePagination({
		queryKey: ['tasks'],
		queryFn: (page, size) => getTasks(uuid, page, size),
		size: 10,
		enabled: !!uuid,
	});

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

	function moveTaskUp(index){

	}

	function moveTaskDown(index){

	}

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
				<ol className='w-full px-4 flex flex-col items-center gap-4'>
					{
						tasks.map((task, index) => 
							<TaskCard 
								key={task.uuid} 
								text={task.description} 
								index={index}
								onDelete={deleteTask} 
								onMoveUp={moveTaskUp} 
								onMoveDown={moveTaskDown}
							/>
						)
					}
				</ol>
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