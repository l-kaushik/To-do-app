import React from 'react'

function SwaggerUi() {
  return (
    <iframe
    src= {`${import.meta.env.VITE_API_URL}swagger-ui/index.html`}
    title='Swagger UI'
    style={{
        width: "100%",
        height: "100vh",
        border: "none"
    }}
    />
  );
}

export default SwaggerUi