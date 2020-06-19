import Axios from 'axios';
const ENDPOINTS = {
    CREATE: 'review',
    ACCEPT_REVIEW: "review/accept-review"
}

export default {
    create(review) {
        return Axios.post(ENDPOINTS.CREATE, review);
    },
    acceptReview(assigmentId, accept) {
        return Axios.post(ENDPOINTS.ACCEPT_REVIEW + "?assignmentId=" + assigmentId + "&accept=" + accept);
    }
}