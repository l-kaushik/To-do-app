import React from 'react'

function WorkspaceCard() {
  return (
    <div className='card-size text-xl md:text-2xl text-white flex flex-col gap-2 p-4 border-gray-500 border-2 rounded-2xl bg-gray-700 min-h-fit transition-all duration-200 hover:scale-[1.03] hover:shadow-lg hover:shadow-black/30'>
        <span className='text-green-500'>Workspace Name</span>
        <hr />
        <ul className="list-disc px-6 text-gray-300">
            <li>12 Tasks</li>
            <li>Last updated 2 days ago</li>
            <li>Last updated task id</li>
            <li>Date created</li>
        </ul>
    </div>
  )
}

export default WorkspaceCard