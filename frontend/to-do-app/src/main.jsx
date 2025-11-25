import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom'
import Layout from './Layout.jsx'
import Home from './components/Home/Home.jsx'
import Workspace from './components/Workspace/Workspace.jsx'
import Login from './components/Login/Login.jsx'

const router = createBrowserRouter(
	createRoutesFromElements(
		<>
		<Route path = "/" element={ <Layout /> }>
			<Route index element={<Home/>}/>
			<Route path = "/workspace/:id" element={<Workspace/>}/>
		</Route>
		<Route path = "/login" element={<Login/>}/>
		</>
	)
);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router = {router} />
  </StrictMode>,
)
