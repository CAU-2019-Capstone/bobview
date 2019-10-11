'use strict'

module.exports = {
    devServer: {
        proxy: {
            '/api': {
                // Forward frontend dev server request for /api to django dev server
                target: 'http://localhost:8000',
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                },
            }
        }
    },
};