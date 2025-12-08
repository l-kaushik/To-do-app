import { useQuery } from '@tanstack/react-query';
import React, { useState, useEffect } from 'react'
import { getUser } from '../../api/todoApi';

function Header() {
    const userQuery = useQuery({
        queryKey: ["userData"],
        queryFn: () => getUser(),
    });

    return (
        <header className='h-20 flex items-center justify-between px-6 bg-gray-900 text-white shadow-md'>
            <div className='text-3xl sm:text-4xl font-extrabold text-green-500'>To-Do App</div>
            <button className="flex space-x-3 px-3 items-center cursor-pointer">
                <div>{userQuery.data?.username ?? "Loading..."}</div>
                <img src="https://picsum.photos/32/32" alt="Profile" className="w-8 h-8 rounded-full" />
            </button>
        </header>
    )
}

export default Header