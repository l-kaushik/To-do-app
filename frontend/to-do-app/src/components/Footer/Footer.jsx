import React from 'react'

function Footer() {
	const currentYear = new Date().getFullYear();
  return (
    <footer className="h-20 flex items-center justify-center px-6 bg-gray-900 text-white shadow-md">
      <p className="text-sm">
        &copy; {currentYear} To-Do App. All rights reserved.
      </p>
    </footer>
  )
}

export default Footer