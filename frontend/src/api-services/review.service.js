import Axios from 'axios';
const ENDPOINTS = {
    CREATE: 'review'
}

export default {
    create(review) {
        return Axios.post(ENDPOINTS.CREATE, review);
    }
}