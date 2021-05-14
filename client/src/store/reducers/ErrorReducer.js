import { ACTIVATION_ERRORS, GET_ERRORS, IS_LOADING, LOGIN_ERRORS, OAUTH2_ERRORS } from "../actions/actionTypes";

const INITIAL_STATE = {
    isLoading:false,
    hasErrors:false,
    hasOauthErrors:false,
    hasActivationErrors:false,
    hasLoginErrors:false,
    errorHandler:{}
};
 
// eslint-disable-next-line import/no-anonymous-default-export
export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case GET_ERRORS:
            return {...state,
                isLoading:false,
                errorHandler:action.payload, 
                hasErrors:true
            }
        case ACTIVATION_ERRORS:{
            return{...state,
                isLoading:false,
                hasActivationErrors:true,
                errorHandler:action.payload
            }
        }
        case LOGIN_ERRORS:{
            return{...state,
                isLoading:false,
                hasLoginErrors:true,
                errorHandler:action.payload
            }
        }
        case OAUTH2_ERRORS:{
            return{...state,
                isLoading:false,
                hasOauthErrors:true,
                errorHandler:action.payload
            }
        }
          
            case IS_LOADING:
                return {...state,
                    isLoading:true
                }
        default:
            return state
    }
   
}