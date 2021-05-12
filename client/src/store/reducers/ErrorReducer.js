import { GET_ERRORS, IS_LOADING } from "../actions/actionTypes";

const INITIAL_STATE = {
    isLoading:false,
    hasErrors:false,
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
            case IS_LOADING:
                return {...state,
                    isLoading:true
                }
        default:
            return state
    }
   
}