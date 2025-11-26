import React, { useState } from 'react'

function Login() {
	const [isSignUp, setIsSignUp] = useState(false);
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
					/>
					<input
						type="email"
						placeholder="Email"
						className="p-2 rounded bg-gray-800 border border-gray-700"
					/>
					</>
				)}

				{/* Sign In only field */}
				{!isSignUp && (
					<input
						type="text"
						placeholder="Username or Email"
						className="p-2 rounded bg-gray-800 border border-gray-700"
					/>
				)}

				{/* Shared Fields */}
				<input
					type="password"
					placeholder="Password"
					className="p-2 rounded bg-gray-800 border border-gray-700"
				/>

				<button className="mt-3 py-2 bg-blue-600 rounded hover:bg-blue-700 transition">
					{isSignUp ? "Create Account" : "Sign In"}
				</button>

				</form>
			</div>
		 </div>
    )
}

export default Login