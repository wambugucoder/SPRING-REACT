import { IS_LOADING, REGISTER_USER } from "../actions/actionTypes";

const INITIAL_STATE = {
    isLoading:false,
    isRegistered:false,
    
};
 
// eslint-disable-next-line import/no-anonymous-default-export
export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case IS_LOADING:
            return {...state,
            isLoading:true,
            isRegistered:false,
            }
        case REGISTER_USER:
                return {...state,
                isLoading:false,
                isRegistered:true,
                }
        default:
            return state
    }
}