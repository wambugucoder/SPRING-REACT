import axios from "axios";
const SetAuthToken=token => {
if (token){
 // Apply authorization token to every request if logged in
 axios.defaults.headers.common["Authorization"]=token;
} else {
 // Delete Auth header
 delete axios.defaults.headers.common["Authorization"];
};
}
export default SetAuthToken;