import React from 'react'
import { useParams } from 'react-router-dom'

function Workspace() {
    const {id} = useParams();
  return (
    <div>Workspace with {id}</div>
  )
}

export default Workspace