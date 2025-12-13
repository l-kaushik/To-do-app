import React, { useEffect, useState, useRef } from 'react'
import WorkspaceCard from '../Workspace/WorkspaceCard.jsx'
import { useQuery } from '@tanstack/react-query';
import { getFullWorkspaces } from '../../api/todoApi.js';
import useInfinitePagination from '../../utils/useInfinitePagination.js';

function Home() {
	const handleAddCard = () => {
		console.log("card added");
		// TODO: show a pop up to create workspace then send a api call of post create workspace
	}

	const {
		items: workspaces,
		bottomRef,
		isLoading,
		isError,
	} = useInfinitePagination({
		queryKey: ['workspaces'],
		queryFn: (page, size) => getFullWorkspaces(page, size),
		size: 10,
	});

	return (
		<main className='min-h-dvh bg-gray-800'>
			<div className="flex flex-wrap justify-center gap-6 p-6">
				<button onClick={handleAddCard} className="bg-gray-900 cursor-pointer flex items-center justify-center p-4 border-2 border-dashed border-gray-600 rounded-2xl min-h-[180px] card-size text-xl md:text-2xl text-gray-400 hover:text-white hover:border-green-500 hover:bg-gray-700 transition-all">
					+ Add Workspace
				</button>

				{isLoading && <p className='text-white text-2xl'>Loading...</p>}
				{isError && <p className='text-red-500 text-2xl'>*Error fetching workspaces</p>}

				{workspaces.map((workspace) => (
					<WorkspaceCard 
						key={workspace.uuid}
						uuid={workspace.uuid}
						name={workspace.name}
						taskCount={workspace.tasksCount}
					 />
				))}
				{/* sentinel element */}
				<div ref={bottomRef}></div>
			</div>
		</main>
	)
}

export default Home