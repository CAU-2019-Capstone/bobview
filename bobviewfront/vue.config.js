'use strict'

module.exports = {
    devServer: {
        proxy: {
            '/api': {
                // Forward frontend dev server request for /api to django dev server
                target: 'http://13.124.90.6:8000',
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                },
            }
        }
    },
};