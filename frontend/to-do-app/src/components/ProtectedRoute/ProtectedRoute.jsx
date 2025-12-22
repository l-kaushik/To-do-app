import {useEffect, useState} from 'react'
import { Navigate, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { getUser } from "../../api/todoApi";

function ProtectedRoute({ children }) {

    //TODO: implement better design

    const userQuery = useQuery({
        queryKey: ["userData"],
        queryFn: getUser,
    });

    const navigate = useNavigate();
    const [redirecting, setRedirecting] = useState(false);

    if(userQuery.isLoading) return <div>Loading...</div>;

    if(userQuery.isError || !userQuery.data) {
        if(!redirecting) {
            setRedirecting(true);

            setTimeout(() => {
                navigate("/login", {replace: true});
            }, 3000)
        }

        return <div>Redirecting to login page...</div>;
    }

    return children;
}

export default ProtectedRoute