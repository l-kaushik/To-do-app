import React from 'react'
import { useSortable } from '@dnd-kit/sortable'
import { CSS } from '@dnd-kit/utilities';

function TaskCard({id, text, index, isCompleted, onChange, onDelete}) {
   const {attributes, listeners, setNodeRef, transform, transition} = useSortable({id});

   const style = {
    transition,
    transform: CSS.Transform.toString(transform),
   };
   
    return (
        <div ref={setNodeRef} {...attributes} style={style} className='w-full lg:w-[75%] bg-gray-600 hover:bg-gray-700 text-white flex items-center gap-4 p-4 rounded-2xl touch-none'>
            <span {...listeners} 
                className={`flex-1 text-center whitespace-pre-wrap truncate ${isCompleted ? 'line-through text-gray-400' : ''}`}>
                    {text}
            </span>
            <div className='flex gap-2 justify-end items-center'>
                <input className={`w-6 h-6 cursor-pointer justify-center align-middle ${ isCompleted ? 'accent-gray-400' : 'accent-green-500'}`} 
                    type='checkbox' checked={isCompleted} 
                    onChange={() => onChange(id, !isCompleted)}>
                </input>
                <button className={` p-2 rounded-sm ${isCompleted ? ' bg-gray-400 hover:bg-gray-500' : 'bg-red-500 hover:bg-red-700'}`} 
                    onClick={() => onDelete(id)}>Delete
                </button>
            </div>
        </div>
    )
}

export default TaskCard