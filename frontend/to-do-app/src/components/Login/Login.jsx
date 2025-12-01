import React, { useState } from 'react'
import { apiClient } from '../../api/apiClient';
import { isEmptyString } from '../../utils/dataValidation';

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

		try {
			// register
			if(isSignUp) {
				const requestbody = {
					username: username,
					emailId: email,
					password: password
				}
				const data = await apiClient("/api/users/register", "POST", requestBody)
				console.log("Reponse: ", data);
				return;
			}


			// login
			const requestBody = {
				identifier: username,
				password: password
			}
			
			const data = await apiClient("/api/users/login", "POST", requestBody)
			console.log("Reponse: ", data);
		} catch (err) {
			if(err.type === "NETWORK_ERROR") {
				setErrorBox(err.message);
				return;
			}

			if(err.status === 401) {
				setErrorBox(err.message);

				if(err.message.includes("email") || err.message.includes("username")) {
					setErrors(prev => ({...prev, identifier: true}));
				}

				if(err.message.includes("password")) {
					setErrors(prev => ({...prev, password: true}));
				}
			}
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

				<button className="mt-3 py-2 bg-blue-600 rounded hover:bg-blue-700 transition"
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
