import axios from "axios";
import { GET_ERRORS, IS_LOADING, REGISTER_USER } from "./actionTypes";


export const RegisterUser = (UserData) =>  dispatch => {
    console.log("iyuguviuf")
    dispatch({
        type: IS_LOADING
    })
    axios.post("/api/v1/auth/signup",UserData).then(result => {
        dispatch({
            type:REGISTER_USER,
            payload:result.data
        })
        
    }).catch((err) => {
        dispatch({
            type:GET_ERRORS ,
            payload: err.response.data
          })
    });
    
    
};

