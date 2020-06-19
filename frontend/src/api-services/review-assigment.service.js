import Axios from 'axios';
const ENDPOINTS = {
    MY_ASSIGMENTS: 'science-paper/my-assigments',
    PAPERS_TO_REVIEW: 'science-paper/papers-to-review'
}

export default {
    getMyAssigments() {
        return Axios.get(ENDPOINTS.MY_ASSIGMENTS);
    },
    getPapersToReview() {
        return Axios.get(ENDPOINTS.PAPERS_TO_REVIEW);
    }
}