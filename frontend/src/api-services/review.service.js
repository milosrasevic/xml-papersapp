import Axios from 'axios';
const ENDPOINTS = {
    CREATE: 'review',
    GET_REVIEWS_FOR_SP: "review/science-paper/"
}

export default {
    create(review) {
        return Axios.post(ENDPOINTS.CREATE, review);
    },
    getReviewsForSciencePaperId(id) {
        return Axios.get(ENDPOINTS.GET_REVIEWS_FOR_SP + id);
    }
}