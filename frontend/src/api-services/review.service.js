import Axios from 'axios';
const ENDPOINTS = {
    CREATE: 'review',
    ASSIGN: 'review/assign-reviewers'
}

export default {
    create(review) {
        return Axios.post(ENDPOINTS.CREATE, review);
    },

    assignReviewers(data) {
        return Axios.post(ENDPOINTS.ASSIGN, data);
    }
}