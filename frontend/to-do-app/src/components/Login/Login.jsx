import React, { useState } from 'react'
import { useMutation } from '@tanstack/react-query'
import { isEmptyString } from '../../utils/dataValidation';
import { loginUser, registerUser } from '../../api/todoApi';
import { useNavigate } from 'react-router-dom';

export default function Login() {
	const [isSignUp, setIsSignUp] = useState(false);
	const [username, setUsername] = useState('');
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');
	const [errorBox, setErrorBox] = useState('');
	const [errors, setErrors] = useState({
		username: false,
		email: false,
		identifier: false,
		password: false
	});
	const navigate = useNavigate();

	function handleNetworkError(){
		setErrorBox("Can't connect to the server. Please check your internet connection.");
	}

	const registerMutation = useMutation({
		mutationFn: (body) => registerUser(body),
		onSuccess: (data) => {
			console.log("Registration success: ", data);
		},
		onError: (error) => {
			if(error.type == "NETWORK_ERROR") {
				handleNetworkError();
			}
		}
	});

	const loginMutation = useMutation({
		mutationFn: (body) => loginUser(body),
		onSuccess: (data) => {
			navigate("/");
		},
		onError: (error) => {
			if(error.type === "NETWORK_ERROR") {
				handleNetworkError();
				return;
			}

			if(error.type === "Unauthorized") {
				setErrorBox(error.message);

				if(error.message.includes("password")) {
					setErrors(prev => ({ ...prev, password: true }));
				}
				else if(error.message.includes("email") || error.message.includes("username")) {
					setErrors(prev => ({ ...prev, identifier: true }));
				}
			}
		},
	});

	async function handleAuth(e){
		e.preventDefault();		// prevent react refresh on form submit
		setErrorBox("");
		setErrors({
			username: false,
			email: false,
			identifier: false,
			password: false
		});

		const validationErrors = validateAuthData({isSignUp, username, email, password});
		if(Object.keys(validationErrors).length > 0) {
			setErrors(prev => ({...prev, ...validationErrors}));
			return;
		}

		// register
		if(isSignUp) {
			const requestBody = {
				username: username,
				emailId: email,
				password: password
			}
			registerMutation.mutate(requestBody);
			return;
		}
		else{
			// login
			const requestBody = {
				identifier: username,
				password: password
			}
				
			loginMutation.mutate(requestBody);
		}
	}

    return (
		 <div className="min-h-screen flex items-center justify-center bg-black text-white">
			<div className="bg-gray-900 p-8 rounded-xl shadow-lg w-80">
				{/* Toggle button */}
				<div className="flex mb-6">
					<button
						className={`flex-1 py-2 ${!isSignUp ? "bg-gray-700" : "bg-gray-800"} rounded-l hover:bg-gray-600 transition`}
						onClick={() => setIsSignUp(false)}
					>
						Sign In
					</button>
					<button
						className={`flex-1 py-2 ${isSignUp ? "bg-gray-700" : "bg-gray-800"} rounded-r hover:bg-gray-600 transition`}
						onClick={() => setIsSignUp(true)}
					>
						Sign Up
					</button>
				</div>
				{/* Form */}
				<form className="flex flex-col gap-4">

				{/* Sign Up only fields */}
				{isSignUp && (
					<>
					<input
						type="text"
						placeholder="Username"
						className={`p-2 rounded bg-gray-800 border ${errors.username ? "border-red-500" : "border-gray-700"}`}
						id="username"
						value={username}
						onChange={(event) => setUsername(event.target.value)}
					/>
					<input
						type="email"
						placeholder="Email"
						className={`p-2 rounded bg-gray-800 border ${errors.email ? "border-red-500" : "border-gray-700"}`}
						id="email"
						value={email}
						onChange={(event) => setEmail(event.target.value)}
					/>
					</>
				)}

				{/* Sign In only field */}
				{!isSignUp && (
					<input
						type="text"
						placeholder="Username or Email"
						className={`p-2 rounded bg-gray-800 border ${errors.identifier ? "border-red-500" : "border-gray-700"}`}
						id="identifier"
						value={username}
						onChange={(event) => setUsername(event.target.value)}
					/>
				)}

				{/* Shared Fields */}
				<input
					type="password"
					placeholder="Password"
					className={`p-2 rounded bg-gray-800 border ${errors.password ? "border-red-500" : "border-gray-700"}`}
					id="password"
					value={password}
					onChange={(event) => setPassword(event.target.value)}
				/>

				<button className="mt-3 py-2 bg-blue-600 rounded hover:bg-blue-700 transition
				active:bg-blue-600
				disabled:bg-blue-400 disabled:cursor-not-allowed"
				disabled={isSignUp ? registerMutation.isPending : loginMutation.isPending}
				onClick={handleAuth}>
					{isSignUp ? "Create Account" : "Sign In"}
				</button>

				</form>

				<div className='pt-4 text-red-600' id='errorBox'>{errorBox}</div>
			</div>
		 </div>
    )
}

function validateAuthData({ isSignUp, username, email, password }) {
	const errors = {};

	if (!isSignUp && isEmptyString(username)) errors.identifier = true;
	if (isSignUp && isEmptyString(username)) errors.username = true;
	if (isSignUp && isEmptyString(email)) errors.email = true;
	if (isEmptyString(password)) errors.password = true;

	return errors;
}
