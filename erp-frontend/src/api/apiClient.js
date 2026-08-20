const BASE_URL = '/api';

export async function apiFetch(endpoint, options = {}) {
  const response = await fetch(`${BASE_URL}${endpoint}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  });

  if (response.status === 204) return null;

  const data = await response.json();
  if (!response.ok) {
    const errorMsg = data.message || (data.fieldErrors ? data.fieldErrors.join(', ') : 'API Request Failed');
    throw new Error(errorMsg);
  }
  return data;
}

export const api = {
  get: (url) => apiFetch(url, { method: 'GET' }),
  post: (url, body) => apiFetch(url, { method: 'POST', body: JSON.stringify(body) }),
  put: (url, body) => apiFetch(url, { method: 'PUT', body: JSON.stringify(body) }),
  delete: (url) => apiFetch(url, { method: 'DELETE' }),
};
