import Axios from 'axios';
const ENDPOINTS = {
  SEARCH_SCIENCE_PAPER: 'science-paper/search',
  SEARCH_SCIENCE__PAPER_AUTHENTICATED: 'science-paper/authenticated/search',
  SUBMIT_SCIENCE_PAPER: "science-paper"
}

export default {
    getSciencePapers(searchParam, isEditor) {
      
      console.log('here');
      let {text, dateFrom, dateTo, state} = searchParam;
      if (isEditor) {
        return Axios.get(ENDPOINTS.SEARCH_SCIENCE__PAPER_AUTHENTICATED + "?text=" + text + "&date-from=" + dateFrom + "&date-to=" + dateTo + "&state=" + state);
      } else {
        return Axios.get(ENDPOINTS.SEARCH_SCIENCE_PAPER + "?text=" + text + "&date-from=" + dateFrom + "&date-to=" + dateTo);
      }
    },
    submit(sciencePaper) {
      return Axios.post(ENDPOINTS.SUBMIT_SCIENCE_PAPER, sciencePaper, {headers: {'Content-Type': 'text/xml'}});
    }
    
}