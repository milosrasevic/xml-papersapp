import Axios from 'axios';
const ENDPOINTS = {
  SEARCH_SCIENCE_PAPER: 'science-paper/search',
  SEARCH_SCIENCE__PAPER_AUTHENTICATED: 'science-paper/authenticated/search',
  SUBMIT_SCIENCE_PAPER: "science-paper",
  GET_SCIENCE_PAPER: 'science-paper/get/',
  CANCEL_SUBMITION: "science-paper/cancel-submission",
  DECIDE: "science-paper/decide"
}

export default {
    getSciencePapers(searchParam, isEditor) {

        let { text, dateFrom, dateTo, state } = searchParam;
        if (isEditor) {
            return Axios.get(ENDPOINTS.SEARCH_SCIENCE__PAPER_AUTHENTICATED + "?text=" + text + "&date-from=" + dateFrom + "&date-to=" + dateTo + "&state=" + state);
        } else {
            return Axios.get(ENDPOINTS.SEARCH_SCIENCE_PAPER + "?text=" + text + "&date-from=" + dateFrom + "&date-to=" + dateTo);
        }
    },
    get(title) {
        return Axios.get(ENDPOINTS.GET_SCIENCE_PAPER + title);
    },
    submit(sciencePaper) {
      return Axios.post(ENDPOINTS.SUBMIT_SCIENCE_PAPER, sciencePaper, {headers: {'Content-Type': 'text/xml'}});
    },
    cancel(title){
        return Axios.delete(ENDPOINTS.CANCEL_SUBMITION + "?title=" + title);
    },
    decide(paperTitle, accepted) {
        return Axios.post(ENDPOINTS.DECIDE, {paperTitle, accepted});
    }
}