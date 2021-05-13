import axios from "axios";
import { ACTIVATE_USER_ACCOUNT, ACTIVATION_ERRORS, GET_ERRORS, IS_LOADING, REGISTER_USER } from "./actionTypes";


export const RegisterUser = (UserData) =>  dispatch => {
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

export const ActivateUserAccount = (token) => dispatch=>{
    dispatch({
        type:IS_LOADING
    })
    axios.put(`/api/v1/auth/activate/${token}`).then((result) => {
        dispatch({
            type:ACTIVATE_USER_ACCOUNT,
            payload:result.data
        })
        
    }).catch((err) => {
        dispatch({
            type:ACTIVATION_ERRORS,
            payload: err.response.data
          })
    });

};
