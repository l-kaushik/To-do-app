import React from 'react'
import { useSortable } from '@dnd-kit/sortable'
import { CSS } from '@dnd-kit/utilities';

function TaskCard({id, text, index, onDelete}) {
   const {attributes, listeners, setNodeRef, transform, transition} = useSortable({id});

   const style = {
    transition,
    transform: CSS.Transform.toString(transform),
   };
   
    return (
        <div ref={setNodeRef} {...attributes} {...listeners} style={style} className='w-full lg:w-[75%] bg-gray-600 hover:bg-gray-700 text-white flex items-center gap-4 p-4 rounded-2xl touch-none'>
            <span className='flex-1 text-center whitespace-pre-wrap truncate'>{text}</span>
            <div className='flex gap-2 justify-end'>
                <button disabled className='bg-red-500 hover:bg-red-700 p-2 rounded-sm disabled:bg-red-300 disabled:cursor-not-allowed' onClick={() => onDelete(index)}>Delete</button>
            </div>
        </div>
    )
}

export default TaskCard