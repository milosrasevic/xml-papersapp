import Axios from 'axios';
const ENDPOINTS = {
  SUBMIT_COVER_LETTER: "cover-letter"
}

export default {
    submit(letter) {
      return Axios.post(ENDPOINTS.SUBMIT_COVER_LETTER, letter, {headers: {'Content-Type': 'text/xml'}});
    }
    
}