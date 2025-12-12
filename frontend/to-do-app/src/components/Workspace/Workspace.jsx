import React, { useState, useEffect, useRef } from 'react'
import { useParams } from 'react-router-dom'
import TaskCard from './TaskCard.jsx'
import { getTasks } from '../../api/todoApi.js';
import { useQuery } from '@tanstack/react-query';

function Workspace() {
	const {uuid} = useParams();   // workspace uuid

	const [allTasks, setAllTasks] = useState([]);
	const [newTask, setNewTask] = useState("");
	const [page, setPage] = useState(0);
	const [totalPage, setTotalPage] = useState(0);
	const size = 5;

	function handleInputChange(event) {
		setNewTask(event.target.value);
	}

	function addTask(){
		if(newTask.trim() === "") return;
		setAllTasks(t => [...t, newTask]);
		setNewTask("")
	}

	function deleteTask(index){
		setAllTasks(t => t.filter((_, i) => i !== index));
	}

	function moveTaskUp(index){
		if(index === 0) return;

		const updatedTasks = [...allTasks];
		[updatedTasks[index], updatedTasks[index - 1]] = [updatedTasks[index - 1], updatedTasks[index]];
		setAllTasks(updatedTasks);
	}

	function moveTaskDown(index){
		if(index === tasks.length - 1) return;

		const updatedTasks = [...allTasks];
		[updatedTasks[index], updatedTasks[index + 1]] = [updatedTasks[index + 1], updatedTasks[index]];
		setAllTasks(updatedTasks);
	}

	const taskQuery = useQuery({
		queryKey: ["task", page],
		queryFn: () => getTasks(uuid, page, size),
		keepPreviousData: true,
	});

	const tasks = taskQuery.data?.content || [];

	useEffect(() => {
		if(taskQuery.data?.content) {
			setAllTasks(prev => [...prev, ...taskQuery.data.content]);
			setTotalPage(taskQuery.data.page.totalPages);
		}
	}, [taskQuery.data]);

	// Intersection Observer ref
	const bottomRef = useRef(null);

	useEffect(() => {
		if(!bottomRef.current) return;
		if(page > totalPage) return;
		const observer = new IntersectionObserver((entries) => {
			const entry = entries[0];
			if(entry.isIntersecting && !taskQuery.isFetching) {
				setPage(prev => prev + 1);
			}
		});
		observer.observe(bottomRef.current);

		return () => observer.disconnect();
	}, [taskQuery.isFetching]);

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
						allTasks.map((task, index) => 
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
				{/* sentinel element */}
				<div ref={bottomRef}></div>
			</div>
		</div>
	)
}

export default Workspace