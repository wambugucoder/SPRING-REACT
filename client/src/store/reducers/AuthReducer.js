import { ACTIVATE_USER_ACCOUNT, IS_LOADING, LOGIN_USER, LOGOUT_USER, REGISTER_USER } from "../actions/actionTypes";

const INITIAL_STATE = {
    isLoading:false,
    isRegistered:false,
    isAccountActivated:false,
    isAuthenticated:false,
    user:{},
};
 
// eslint-disable-next-line import/no-anonymous-default-export
export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case IS_LOADING:
            return {...state,
            isLoading:true,
            isRegistered:false,
            isAccountActivated:false,
            }
        case REGISTER_USER:
                return {...state,
                isLoading:false,
                isRegistered:true,
                }
        case ACTIVATE_USER_ACCOUNT:
            return{...state,
            isLoading:false,
            isAccountActivated:true,
            }
        case LOGIN_USER:
            return{...state,
            isLoading:false,
            isAuthenticated:true,
            user:action.payload
            }
        case LOGOUT_USER:
            return{...state,
            isLoading:false,
            isAuthenticated:false,
            user:{}
            }
        default:
            return state
    }
}