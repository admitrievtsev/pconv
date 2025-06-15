# Image Convolution

## Commands:

    Help list. Commands:
    /exit - to exit program
    /load [path] - to load image. Path cannot be null.
    /save [path] - to save image. Path cannot be null.
    /help - to call list of possible commands
    /stream [filter type] [path1] [path2] ... - on-line processing of array of images
        [filter type] - type of filter applying to image. Possible variants:
    Note: you current images will be replaced with new images by time they will be processed
    /convolution [filter type] [parallel type] [amount of threads] - to make stored image convolution.
        [filter type] - type of filter applying to image. Possible variants:
            - blur
            - gaussian_blur
            - motion_blur
            - find_edges
            - sharpen
            - emboss
            - id
        [parallel type] - type of parallel convolution execution, Possible variants:
            - 1(sequential)
            - 2(pixel)
            - 3(rows)
            - 4(cols)
        [amount of thread] - amount of threads. Any integer umber >1. Note: if you want to use 1(sequential) type of parallelism, make this option equal to any integer number
    WARNING: YOU CANNOT MAKE CONVOLUTION WITHOUT LOADING IMAGE BEFORE IT

## Experiment

You can find experimental results in experiments_updated.pdf
