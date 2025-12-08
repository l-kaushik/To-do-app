import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom'
import Layout from './Layout.jsx'
import Home from './components/Home/Home.jsx'
import Workspace from './components/Workspace/Workspace.jsx'
import Login from './components/Login/Login.jsx'
import "./index.css";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import ProtectedRoute from './components/ProtectedRoute/ProtectedRoute.jsx'

const router = createBrowserRouter(
	createRoutesFromElements(
		<>
		<Route path = "/" element={ 
			<ProtectedRoute>
				<Layout /> 
			</ProtectedRoute>
			}>
			<Route index element={<Home/>}/>
			<Route path = "/workspace/:id" element={<Workspace/>}/>
		</Route>
		<Route path = "/login" element={<Login/>}/>
		</>
	)
);

const queryClient = new QueryClient();

createRoot(document.getElementById('root')).render(
	<QueryClientProvider client={queryClient}>
		<StrictMode>
			<RouterProvider router = {router} />
		</StrictMode>
	</QueryClientProvider>
)
