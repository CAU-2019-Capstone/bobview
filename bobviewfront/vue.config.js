'use strict'
var fs = require('fs')

module.exports = {
    devServer: {
        host: '0.0.0.0',
        hot: true,
        disableHostCheck: true,
        https: {
            key: fs.readFileSync('path/bobview.com.key'),
            cert: fs.readFileSync('path/bobview.com.crt'),
            ca: fs.readFileSync('path/rootca.crt'),
        },
        proxy: {
            '/api': {
                // Forward frontend dev server request for /api to django dev server
                target: 'https://13.124.90.6:8080',
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                },
            }
        }
    },
};