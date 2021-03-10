// src/access.ts
export default function access(initialState: { currentUser?: any }) {
  const { currentUser } = initialState || {};
  return {
    canAdmin: currentUser && currentUser.access === 'admin',
  };
}
