import React from 'react'
import { useNavigate } from 'react-router-dom'

function WorkspaceCard({uuid, name, taskCount, onDelete}) {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/workspace/${uuid}`);
  }

  return (
    <div onClick={handleClick} className='card-size text-xl md:text-2xl text-white flex flex-col gap-2 p-4 border-gray-500 border-2 rounded-2xl bg-gray-700 min-h-fit transition-all duration-200 hover:scale-[1.03] hover:shadow-lg hover:shadow-black/30 hover:cursor-pointer'>
        <span className='text-2xl text-green-500'>{name}</span>
        <hr />
        <ul className="list-disc px-6">
            <li className='text-xl text-gray-400'>UUID: <p className='inline text-white'>{uuid}</p></li>
            <li className='text-xl text-gray-400'>No. Of Tasks: <p className='inline text-white'>{taskCount}</p></li> 
        </ul>
        <button className={`text-xs p-2 rounded-sm bg-red-500 hover:bg-red-700`} 
        onClick={(e) =>{
          e.stopPropagation();
          onDelete(uuid)}
        }
        >Delete</button>
    </div>
  )
}

export default WorkspaceCard