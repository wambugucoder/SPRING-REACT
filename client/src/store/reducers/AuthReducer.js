import { ACTIVATE_USER_ACCOUNT, IS_LOADING, REGISTER_USER } from "../actions/actionTypes";

const INITIAL_STATE = {
    isLoading:false,
    isRegistered:false,
    isAccountActivated:false,
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
        default:
            return state
    }
}