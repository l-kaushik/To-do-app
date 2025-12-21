import React, { useEffect, useState, useRef } from 'react'
import WorkspaceCard from '../Workspace/WorkspaceCard.jsx'
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { createWorkspace, getFullWorkspaces, removeWorkspace } from '../../api/todoApi.js';
import useInfinitePagination from '../../utils/useInfinitePagination.js';
import CreateWorkspaceModal from '../Workspace/CreateWorkspaceModal.jsx';

function Home() {
	const [showModal, setShowModal] = useState(false);
	const queryClient = useQueryClient();

	const handleAddCard = () => {
		setShowModal(true);
	}

	const {
		mutate: deleteWorkspace,
	} = useMutation({
		mutationFn: (uuid) => removeWorkspace(uuid),
		onSuccess: () => {
			queryClient.invalidateQueries({queryKey: ['workspaces']});
		} 
	})

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

	const { 
		mutate, 
		isLoading: isCreating, 
		error: createError,
		isError: isCreateError
	 } = useMutation({
		mutationFn: (data) => createWorkspace(data),
		onSuccess: () => {
			setShowModal(false);
			
			queryClient.invalidateQueries({ queryKey: ['workspaces'] });
		},
	});

	const createErrorMessage = () => {
		if(!isCreateError) return null;

		if(createError.type === "CONFLICT") return createError.message;
		else if(createError.type === "BAD_REQUEST"){
			const errors = createError?.message?.errors;

			if(errors && typeof errors === 'object') {
				return errors ? Object.values(errors)[0] : 'Invalid input';
			}
		}

		return "Error creating workspace";
	}

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
						onDelete={deleteWorkspace}
						taskCount={workspace.tasksCount}
					 />
				))}
				{/* sentinel element */}
				<div ref={bottomRef}></div>
			</div>
			{showModal && (
				<CreateWorkspaceModal
					onClose={() => setShowModal(false)}
					onSave={(data) => mutate(data)}
					isLoading={isCreating}
					error={createErrorMessage()}
				/>
			)}
		</main>
	)
}

export default Home