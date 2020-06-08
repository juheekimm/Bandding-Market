import axios from "axios";

export default axios.create({
    baseURL: "http://k02a3031.p.ssafy.io:8080",
    headers: {
        "Content-type": "application/json",
        'Access-Control-Allow-Origin': '*',
        'exposedHeaders': 'jwt-auth-token',
        'jwt-auth-token': '',
        'Access-Control-Expose-Headers': 'jwt-auth-token',

    }
});