import React, { useState } from 'react'

function Login() {
	const [isSignUp, setIsSignUp] = useState(false);
	const [username, setUsername] = useState('');
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');

	async function handleAuth(e){
		e.preventDefault();		// prevent react refresh on form submit

		if(isSignUp) {
			// handle register api call
			return;
		}

		// handle login api call
		const requestBody = {
			identifier: username,
			password: password
		}

		try {
			const res = await fetch("http://localhost:8080/api/users/login", {
				method: "POST",
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify(requestBody)
			});

			if(!res.ok) {
				const text = await res.text();
				console.error("Failed response: ", text);
				return;
			}

			const data = await res.json();
			console.log("Reponse: ", data);
		} catch (err) {
			console.error("Network Error: ", err);
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
						className="p-2 rounded bg-gray-800 border border-gray-700"
						id="username"
						value={username}
						onChange={(event) => setUsername(event.target.value)}
					/>
					<input
						type="email"
						placeholder="Email"
						className="p-2 rounded bg-gray-800 border border-gray-700"
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
						className="p-2 rounded bg-gray-800 border border-gray-700"
						id="identifier"
						value={username}
						onChange={(event) => setUsername(event.target.value)}
					/>
				)}

				{/* Shared Fields */}
				<input
					type="password"
					placeholder="Password"
					className="p-2 rounded bg-gray-800 border border-gray-700"
					id="password"
					value={password}
					onChange={(event) => setPassword(event.target.value)}
				/>

				<button className="mt-3 py-2 bg-blue-600 rounded hover:bg-blue-700 transition"
				onClick={handleAuth}>
					{isSignUp ? "Create Account" : "Sign In"}
				</button>

				</form>
			</div>
		 </div>
    )
}

export default Login