import axios from 'axios';

const BASE_URL = 'http://localhost:8080';

const axiosInstance = axios.create({
    baseURL: BASE_URL,
    withCredentials: true,
});

axiosInstance.interceptors.response.use((response) => response, (error) => {
    const customError = createCustomError(error);
    console.error("API Error: ", customError);
    return Promise.reject(customError);
});

function createCustomError(error){
    if(!error.response && error.request) {
        return {
            type: "NETWORK_ERROR",
            message: "Cannot reach the server, Check your internet connection.",
        };
    }

    const status = error.response.status;
    const data = error.response.data;
    const message = data.message || data || "Invalid Request";

    // 400-level errors
    if (status === 400) {
        return {
            type: "BAD_REQUEST",
            message: message,
        };
    }

    if(status === 401){
        return {
            type: "UNAUTHORIZED",
            message: message,
        }
    }

    if(status === 409){
        return {
            type: "CONFLICT",
            message: message,
        }
    }

    return error.response.data;
}


// Authentication

export const registerUser = async (userData) => {
    const response = await axiosInstance.post("/api/users/", userData);
    return response.data;
};

export const loginUser = async (credentials) => {
    const response = await axiosInstance.post("/api/users/login", credentials);
    return response.data;
}

// user endpoints

export const getUser = async () => {
    const response = await axiosInstance.get("/api/users/me");
    return response.data;
}

// workspace endpoints

export const getFullWorkspaces = async(page = 0, size = 10) => {
    const response = await axiosInstance.get(`/api/workspaces?full=true&page=${page}&size=${size}`);
    return response.data;
}

export const getTasks = async(uuid, page = 0, size = 10) => {
    const response = await axiosInstance.get(`/api/workspaces/${uuid}/tasks?page=${page}&size=${size}`);
    return response.data;
}