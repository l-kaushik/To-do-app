import { useState } from 'react';

function CreateWorkspaceModal({ onClose, onSave, isLoading, error }) {
	const [name, setName] = useState('');

	const handleSubmit = (e) => {
		e.preventDefault();
		onSave({ name, tasks: [] });
	};

	return (
		<div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50">
			<div className="bg-gray-900 rounded-xl p-6 w-full max-w-md">
				<h2 className="text-xl text-white mb-4">Create Workspace</h2>

				<form onSubmit={handleSubmit} className="space-y-4">
					<input
						required
						value={name}
						onChange={(e) => setName(e.target.value)}
						placeholder="Workspace name"
						className="w-full p-2 rounded bg-gray-800 text-white"
					/>
					<p className='text-red-600'>{error}</p>

					<div className="flex justify-end gap-3">
						<button
							type="button"
							onClick={onClose}
							className="text-gray-400 hover:text-white"
						>
							Cancel
						</button>

						<button
							type="submit"
							disabled={isLoading}
							className="bg-green-600 px-4 py-2 rounded text-white disabled:opacity-50"
						>
							{isLoading ? 'Saving...' : 'Save'}
						</button>
					</div>
				</form>
			</div>
		</div>
	);
}

export default CreateWorkspaceModal;
