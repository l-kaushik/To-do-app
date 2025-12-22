import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom';
import { getUser, logoutUser } from '../../api/todoApi';

function Header() {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);

    const userQuery = useQuery({
        queryKey: ["userData"],
        queryFn: () => getUser(),
    });

    const handleProfileClick = () => {
        setOpen(prev => !prev);
    };

    const{ mutate }= useMutation({
        mutationFn: () => logoutUser(),
        onSuccess: () => {
            queryClient.removeQueries(['userData', 'workspaces', 'tasks']);
            navigate("/login");
        },
    });

    return (
        <header className='h-20 flex items-center justify-between px-6 bg-gray-900 text-white shadow-md'>
            <div className='text-3xl sm:text-4xl font-extrabold text-green-500'>To-Do App</div>
            <button className="flex space-x-3 px-3 items-center cursor-pointer" onClick={handleProfileClick}>
                <div>{userQuery.data?.username ?? "Loading..."}</div>
                <img src="https://picsum.photos/32/32" alt="Profile" className="w-8 h-8 rounded-full" />
            </button>
            {open && (
                <div className="absolute right-8 mt-20 w-40 bg-gray-800 border border-gray-700 rounded-md shadow-lg z-50">
                    <button
                    onClick={mutate}
                    className="w-full text-left px-4 py-2 text-sm hover:bg-gray-700 cursor-pointer"
                    >
                    Logout
                    </button>
                </div>
            )}
        </header>
    )
}

export default Header