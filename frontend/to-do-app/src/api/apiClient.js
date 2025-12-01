export const port = "8080";
export const baseUrl = `http://localhost:${port}`;
let isFetching = false;

export async function apiClient(urlPath, method = "GET", body = null) {
    if(isFetching) return;

    const url = baseUrl + urlPath;
    const config = {
        method,
        headers: {
           	"Content-Type": "application/json"
        }
    };

    if(body) {
        config.body = JSON.stringify(body);
    }

    try{
        isFetching = true;
        const res = await fetch(url, config);
        
        if(!res.ok) {

            let raw;

            try{
                raw = await res.json();
            } catch {
                raw = await res.text();
            }

            const errorObject = new Error(raw.message || raw || "Unknown error");
            errorObject.status = res.status;
            errorObject.raw = raw;
            throw errorObject
        }

        return await res.json();
    } catch(err){
        if (err instanceof TypeError) {
            const netErr = new Error("Network error: server unreachable");
            netErr.type = "NETWORK_ERROR";
            throw netErr;
        }
        throw err;
    } finally{
        isFetching = false;
    }
}