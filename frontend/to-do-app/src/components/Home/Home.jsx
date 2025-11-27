import React, { useState } from 'react'
import WorkspaceCard from '../Workspace/WorkspaceCard.jsx'

function Home() {
	// fetch workspaces using api call on first load using effect hook
	const [cards, setCards] = useState([]);

	const handleAddCard = () => {
		console.log("card added");
		// instead of empty object, show a pop up to create workspace then send a api call of post create workspace
		setCards(c => [...c, {}]);
	}

	return (
		<main className='min-h-dvh bg-gray-800'>
			<div className="flex flex-wrap justify-center gap-6 p-6">
				<button onClick={handleAddCard} className="bg-gray-900 cursor-pointer flex items-center justify-center p-4 border-2 border-dashed border-gray-600 rounded-2xl min-h-[180px] card-size text-xl md:text-2xl text-gray-400 hover:text-white hover:border-green-500 hover:bg-gray-700 transition-all">
					+ Add Workspace
				</button>
				{cards.map((_, index) => (
					<WorkspaceCard key={index} />
				))}
			</div>
		</main>
	)
}

export default Home