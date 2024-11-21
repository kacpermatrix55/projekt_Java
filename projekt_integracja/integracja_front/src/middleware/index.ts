export const onRequest: import('astro').MiddlewareHandler = async ({ locals, cookies }, next) => {
    if (!cookies.has('accessToken')) {
        return next();
    }

    locals.accessToken = cookies.get('accessToken').value;

    return next();
};
