export const API_BASE_URL = process.env.NODE_ENV==="development"?'https://localhost:8443/api/v1':'https://pollsapp-2021.westeurope.cloudapp.azure.com:8443/api/v1'
export const OAUTH2_REDIRECT_URI = process.env.NODE_ENV==="development"?'http://localhost:3000/oauth2/redirect':'https://pollsapp-2021.westeurope.cloudapp.azure.com/oauth2/redirect'
export const ACCESS_TOKEN = 'jwtToken';
export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = API_BASE_URL + '/oauth2/authorize/github?redirect_uri=' + OAUTH2_REDIRECT_URI;
