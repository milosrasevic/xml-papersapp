import Axios from 'axios';

const ENDPOINTS = {
    GET_USER: 'users'
}

export default {
    getUser(id) {
        return Axios.get(ENDPOINTS.GET_USER + '/' + id);
    },
    getUsers() {
        return Axios.get(ENDPOINTS.GET_USER + '/getAuthors');
    }
}