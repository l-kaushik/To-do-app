import React from 'react'

function TaskCard({text, index, onDelete, onMoveUp, onMoveDown}) {
    return (
        <div className='w-full lg:w-[75%] bg-gray-600 hover:bg-gray-700 text-white flex items-center gap-4 p-4 rounded-2xl'>
            <span className='flex-1 text-center whitespace-pre-wrap truncate'>{text}</span>
            <div className='flex gap-2 justify-end'>
                <button className='bg-red-500 hover:bg-red-700 p-2 rounded-sm' onClick={() => onDelete(index)}>Delete</button>
                <button className='bg-blue-400 hover:bg-blue-500 p-2 rounded-sm' onClick={() => onMoveUp(index)}>👆</button>
                <button className='bg-blue-400 hover:bg-blue-500 p-2 rounded-sm' onClick={() => onMoveDown(index)}>👇</button>
            </div>
        </div>
    )
}

export default TaskCard