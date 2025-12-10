import React, { useEffect, useState, useRef } from 'react'
import WorkspaceCard from '../Workspace/WorkspaceCard.jsx'
import { useQuery } from '@tanstack/react-query';
import { getFullWorkspaces } from '../../api/todoApi.js';

function Home() {
	const [allWorkspaces, setAllWorkspaces] = useState([]);	// store all pages combined
	const [page, setPage] = useState(0);
	const [totalPage, setTotalPage] = useState(0);
	const size = 10;

	const handleAddCard = () => {
		console.log("card added");
		// TODO: show a pop up to create workspace then send a api call of post create workspace
	}

	const workspaceQuery = useQuery({
		queryKey: ["workspaces", page],
		queryFn: () => getFullWorkspaces(page, size),
		keepPreviousData: true,
	});

	const workspaces = workspaceQuery.data?.content || [];

	useEffect(() => {
		if(workspaceQuery.data?.content) {
			setAllWorkspaces(prev => [...prev, ...workspaceQuery.data.content]);
			setTotalPage(workspaceQuery.data.page.totalPages);
		}
	}, [workspaceQuery.data]);

	// Intersection Observer ref
	const bottomRef = useRef(null);

	useEffect(() => {
		if(!bottomRef.current) return;
		if(page > totalPage) return;
		const observer = new IntersectionObserver((entries) => {
			const entry = entries[0];
			if(entry.isIntersecting && !workspaceQuery.isFetching) {
				setPage(prev => prev + 1);
			}
		});
		observer.observe(bottomRef.current);

		return () => observer.disconnect();
	}, [workspaceQuery.isFetching]);

	return (
		<main className='min-h-dvh bg-gray-800'>
			<div className="flex flex-wrap justify-center gap-6 p-6">
				<button onClick={handleAddCard} className="bg-gray-900 cursor-pointer flex items-center justify-center p-4 border-2 border-dashed border-gray-600 rounded-2xl min-h-[180px] card-size text-xl md:text-2xl text-gray-400 hover:text-white hover:border-green-500 hover:bg-gray-700 transition-all">
					+ Add Workspace
				</button>

				{workspaceQuery.isLoading && <><br/><p>Loading...</p></>}
				{workspaceQuery.isError && <p>Error fetching workspaces</p>}

				{allWorkspaces.map((workspace) => (
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