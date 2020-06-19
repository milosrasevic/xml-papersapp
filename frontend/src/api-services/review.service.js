import Axios from 'axios';
const ENDPOINTS = {
    CREATE: 'review',
    GET_REVIEWS_FOR_SP: "review/science-paper/",
    GET_REVIEW_ASSIGNEMETS_FOR_SP: "review/assignments/science-paper/"
}

export default {
    create(review) {
        return Axios.post(ENDPOINTS.CREATE, review);
    },
    getReviewsForSciencePaperId(id) {
        return Axios.get(ENDPOINTS.GET_REVIEWS_FOR_SP + id);
    },
    getReviewAssignementsForSciencePaperId(id) {
        return Axios.get(ENDPOINTS.GET_REVIEW_ASSIGNEMETS_FOR_SP + id);
    }
}