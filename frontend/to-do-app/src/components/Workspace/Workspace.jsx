import React, { useState } from 'react'
import { useParams } from 'react-router-dom'
import TaskCard from './TaskCard.jsx'

function Workspace() {
	const {id} = useParams();   // workspace id

	// NOTE: upgrade to use linked list for O(1) moving operation

	const [tasks, setTasks] = useState([]);
	const [newTask, setNewTask] = useState("");

	function handleInputChange(event) {
		setNewTask(event.target.value);
	}

	function addTask(){
		if(newTask.trim() === "") return;
		setTasks(t => [...t, newTask]);
		setNewTask("")
	}

	function deleteTask(index){
		setTasks(t => t.filter((_, i) => i !== index));
	}

	function moveTaskUp(index){
		if(index === 0) return;

		const updatedTasks = [...tasks];
		[updatedTasks[index], updatedTasks[index - 1]] = [updatedTasks[index - 1], updatedTasks[index]];
		setTasks(updatedTasks);
	}

	function moveTaskDown(index){
		if(index === tasks.length - 1) return;

		const updatedTasks = [...tasks];
		[updatedTasks[index], updatedTasks[index + 1]] = [updatedTasks[index + 1], updatedTasks[index]];
		setTasks(updatedTasks);
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
								text={task} 
								key={index} 
								index={index}
								onDelete={deleteTask} 
								onMoveUp={moveTaskUp} 
								onMoveDown={moveTaskDown}
							/>
						)
					}
				</ol>
			</div>
		</div>
	)
}

export default Workspace