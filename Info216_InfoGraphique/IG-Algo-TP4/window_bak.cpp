/*
# include <limits>
# include <math.h>
# include "window.hpp"
# include <iostream>

Window::Window()
{
    pixels = NULL;
    pixels_final = NULL;
    width = 300;
    height = 300;
    sample = 1;
    width *= sample;
    height *= sample;
    nb_pixels = width*height;
}

Window::~Window()
{
    if (pixels != NULL)
    {
        delete[] pixels;
    }
    if(pixels_final != NULL)
    {
        delete[] pixels_final;
    }
}

const int Window::get_width()
{
    return width / sample;
}

const int Window::get_height()
{
    return height / sample;
}

unsigned char * Window::get_pixels()
{
    if(sample == 1)
    {
        return pixels;
    }
    else
    {
        return pixels_final;
    }
}

vec3 Window::get_pixel(vec2 p)
{
    if(0<=p.x && p.x<width && 0<=p.y && p.y<height)
    {
        int indice = (p.x + p.y*width) * 3;
        return vec3(c2f(pixels[indice]), c2f(pixels[indice+1]), c2f(pixels[indice+2]));
    }

    return vec3();
}

void Window::draw_pixel(vec2 p, vec3 c)
{
    if(p.x>=0 && p.x<width && p.y>=0 && p.y<height)
    {
        int indice = (p.x + p.y*width) * 3;
        pixels[indice] = f2c(c.x);
        pixels[indice+1] = f2c(c.y);
        pixels[indice+2] = f2c(c.z);
    }
}

void Window::draw_pixel_sampled(vec2 p, vec3 c)
{
    if(0<=p.x && p.x<width && 0<=p.y && p.y<height)
    {
        int indice = (p.x + p.y*width/sample) * 3;
        pixels_final[indice] = f2c(c.x);
        pixels_final[indice+1] = f2c(c.y);
        pixels_final[indice+2] = f2c(c.z);
    }
}

void Window::clear()
{
    //Clear color
    for(int i=0; i<nb_pixels*3; i+=3)
    {
        pixels[i] = 0;
        pixels[i+1] = 0;
        pixels[i+2] = 0;
    }
}

void Window::reshape(int w, int h)
{
    width = w;
    height = h;
    width *= sample;
    height *= sample;
    nb_pixels = width * height;

    if(pixels != NULL)
    {
        delete[] pixels;
    }
    if(pixels_final != NULL)
    {
        delete[] pixels_final;
    }

    pixels = new unsigned char[nb_pixels*3];
    pixels_final = new unsigned char[(nb_pixels/(sample*sample))*3];
    //Clear color
    for(int i=0; i<nb_pixels*3; i+=3)
    {
        pixels[i] = 0;
        pixels[i+1] = 0;
        pixels[i+2] = 0;
    }
}

void Window::antialiasing()
{
    vec3 color;
    for( int x=0; x<width -sample ; x+= sample )
    {
        for( int y=0; y<height -sample ; y+= sample)
        {
            for( int i=0; i< sample ; i++)
            {
                for( int j=0; j<sample ; j++)
                {
                    color += get_pixel(vec2(x+i, y+j));
                }
            }
            color /= sample *sample ;
            draw_pixel_sampled(vec2(x/sample , y/sample ), color);
            color = vec3();
        }
    }
}

void Window::draw_line(vec2 p1, vec2 p2, vec3 c)
{
    int x1 = ( int)p1.x;
    int y1 = ( int)p1.y;
    int x2 = ( int)p2.x;
    int y2 = ( int)p2.y;
    int dx = ( int)abs(x2 - x1);
    int dy = ( int)abs(y2 - y1);
    int xinc = (p2.x>p1.x)?1: -1;
    int yinc = (p2.y>p1.y)?1: -1;
    if(dx >dy)
    {
        int e = -dx;
        int x = (x1 <x2)?x1:x2;
        int y = (x1 <x2)?y1:y2;
        if(x2 <x1)
        {
            yinc = -yinc;
        }
        for( int i=0; i<=dx; i++)
        {
            draw_pixel(vec2(x,y), c);
            e += 2*dy;
            x++;

            if(e > 0)
            {
                y += yinc;
                e -= 2*dx;
            }
        }
    }
    else
    {
        int e = -dy;
        int x = (y1 <y2)?x1:x2;
        int y = (y1 <y2)?y1:y2;
        if(y2 <y1)
        {
            xinc = -xinc;
        }
        for( int i=0; i<=dy; i++)
        {
            draw_pixel(vec2(x,y), c);
            e += 2*dx;
            y++;
            if(e > 0)
            {
                x += xinc;
                e -= 2*dy;
            }
        }
    }
}

void Window::draw_circle(vec2 center, unsigned int r, vec3 c)
{
    int x = 0;
    int y = r;
    int e = 1 - ( double )r;
    draw_circle_parts(vec2(x, y), center , c);
    while(y>x)
    {
        if(e < 0)
        {
            e = e + 2*x + 3;
        }
        else
        {
            e = e + 2*x - 2*y + 5;
            y = y - 1;
        }
        draw_circle_parts(vec2(x, y), center , c);
        x = x + 1;
    }
}

void Window::draw_circle_parts(vec2 p, vec2 center, vec3 c)
{
    draw_pixel( vec2(p.x, p.y)+center , c);
    draw_pixel( vec2(p.x, -p.y)+center , c);
    draw_pixel( vec2(-p.x, p.y)+center , c);
    draw_pixel( vec2(-p.x, -p.y)+ center , c);
    draw_pixel( vec2(p.y, p.x)+center , c);
    draw_pixel( vec2(p.y, -p.x)+center , c);
    draw_pixel( vec2(-p.y, p.x)+center , c);
    draw_pixel( vec2(-p.y, -p.x)+ center , c);
}

void Window::raster_buffer_insert(int x, int raster_buffer[2])
{
	raster_buffer[0] = min(raster_buffer[0], x);
	raster_buffer[1] = max(raster_buffer[1], x);
}

void Window::raster_buffer_insert(int x, int raster_buffer[2], vec3 color, vec3 color_buffer[2])
{
    if(x < raster_buffer[0]) {
        raster_buffer[0] = x;
        color_buffer[0] = color;
    }

    if (x > raster_buffer[1]){
        raster_buffer[1] = x;
        color_buffer[1] = color;
    }
}

void Window::draw_horizontal_line(int y, int x1, int x2, vec3 c)
{
	for(int i=x1; i<=x2; i++)
		draw_pixel(vec2(i, y), c);
}

void Window::draw_horizontal_line(int y, int x1, int x2, vec3 c[2])
{
    for (int i = 0; i <= x2; ++i) {
        if(i == 0){
            draw_pixel(vec2(x1, y), c[0]);
        } else if (i == x2) {
            draw_pixel(vec2(x2, y), c[1]);
        } else {
            vec3 color = ((c[1] - c[0]) * (float)(x1 + i) / (float)x2) + c[0];
            draw_pixel(vec2(x1 + i, y), color);
        }
    }
}

void Window::draw_quad(vec2 p[4], vec3 c)
{
	int raster_buffer[height][2];
	for(int i=0; i<height; i++)
	{
		raster_buffer[i][0] = INT_MAX;
		raster_buffer[i][1] = INT_MIN;
	}
	vec2 p1, p2;
	for(unsigned int i=0; i<4; i++)
	{
		p1 = p[i];
		p2 = p[(i+1)%4];
		int x1 = (int)p1.x;
		int y1 = (int)p1.y;
		int x2 = (int)p2.x;
		int y2 = (int)p2.y;
		int dx = (int)abs(x2 - x1);
		int dy = (int)abs(y2 - y1);
		int xinc = (p2.x>p1.x)?1:-1;
		int yinc = (p2.y>p1.y)?1:-1;
		if(dx == 0 && dy == 0)
		{
			if(y1>=0 && y1<height)
			{
				raster_buffer_insert(x1, raster_buffer[y1]);
			}
		}
		if(dx>dy)
		{
			int e = -dx;
			int x = (x1<x2)?x1:x2;
			int y = (x1<x2)?y1:y2;
			if(x2<x1)
			{
				yinc = -yinc;
			}
			for(int i=0; i<=dx; i++)
			{
				if(y>=0 && y<height)
				{
					raster_buffer_insert(x, raster_buffer[y]);
				}
				e += 2*dy;
				x++;
				if(e > 0)
				{
					y += yinc;
					e -= 2*dx;
				}
			}
		}
		else
		{
			int e = -dy;
			int x = (y1<y2)?x1:x2;
			int y = (y1<y2)?y1:y2;
			if(y2<y1)
			{
				xinc = -xinc;
			}
			for(int i=0; i<=dy; i++)
			{
				if(y>=0 && y<height)
				{
					raster_buffer_insert(x, raster_buffer[y]);
				}
				e += 2*dx;
				y++;
				if(e > 0)
				{
					x += xinc;
					e -= 2*dy;
				}
			}
		}
	}
	for(int i=0; i<height; i++)
	{
		draw_horizontal_line(i, raster_buffer[i][0], raster_buffer[i][1], c);
	}
}

vec3 Window::interpolation(int x1, int x2, int x, vec3 c[2]){
    float x1_f = (float)x1;
    float x2_f = (float)x2;
    float x_f = (float)x;
    vec3 color;

    if(x1 != x2){
        color = (c[0] * ((x_f-x2_f)/(x1_f-x2_f)) + c[1] * ((x1_f - x_f)/(x1_f-x2_f)));*/
/*
        if (color.x > 1.0)
            color.x = 1.0;
        if (color.y > 1.0)
            color.y = 1.0;
        if (color.z > 1.0)
            color.z = 1.0;
        if (color.x < 0.0)
            color.x = 0.0;
        if (color.y < 0.0)
            color.y = 0.0;
        if (color.z < 0.0)
            color.z = 0.0;*//*

    } else {
        return c[0];
    }

    return color;
}

void Window::draw_quad(vec2 p[4], vec3 c[4])
{
	int raster_buffer[height][2];
	vec3 color_buffer[height][2];
	for(int i=0; i<height; i++)
	{
		raster_buffer[i][0] = INT_MAX;
		raster_buffer[i][1] = INT_MIN;
	}
	vec2 p1, p2;
	for(unsigned int i=0; i<4; i++)
	{
		p1 = p[i];
		p2 = p[(i+1)%4];
		int x1 = (int)p1.x;
		int y1 = (int)p1.y;
		int x2 = (int)p2.x;
		int y2 = (int)p2.y;
		int dx = (int)abs(x2 - x1);
		int dy = (int)abs(y2 - y1);
		int xinc = (p2.x>p1.x)?1:-1;
		int yinc = (p2.y>p1.y)?1:-1;
		if(dx == 0 && dy == 0)
		{
			if(y1>=0 && y1<height)
			{
				raster_buffer_insert(x1, raster_buffer[y1], c[i], color_buffer[y1]);
			}
		}
		if(dx>dy)
		{
			int e = -dx;
			int x = (x1<x2)?x1:x2;
			int y = (x1<x2)?y1:y2;
			if(x2<x1)
			{
				yinc = -yinc;
			}
			for(int i=0; i<=dx; i++)
			{
				if(y>=0 && y<height)
				{
				    abs(c[(i+1)%4] - c[i]) * (float)(x1 + i) / (float)x2 + c[(i+1)%4];
					raster_buffer_insert(x, raster_buffer[y], c[i], color_buffer[y]);
				}
				e += 2*dy;
				x++;
				if(e > 0)
				{
					y += yinc;
					e -= 2*dx;
				}
			}
		}
		else
		{
			int e = -dy;
			int x = (y1<y2)?x1:x2;
			int y = (y1<y2)?y1:y2;
			if(y2<y1)
			{
				xinc = -xinc;
			}
			for(int i=0; i<=dy; i++)
			{
				if(y>=0 && y<height)
				{
				    abs(c[(i+1)%4] - c[i]) * (float)(x1 + i) / (float)x2 + c[(i+1)%4];
					raster_buffer_insert(x, raster_buffer[y], c[i], color_buffer[y]);
				}
				e += 2*dx;
				y++;
				if(e > 0)
				{
					x += xinc;
					e -= 2*dy;
				}
			}
		}
	}
	for(int i=0; i<height; i++)
	{
		draw_horizontal_line(i, raster_buffer[i][0], raster_buffer[i][1], color_buffer[i]);
	}
}

unsigned char Window::f2c(float f)
{
    return (unsigned char)(f * 255);
}

float Window::c2f(unsigned char c)
{
    return ((float)c / 255.0f);
}
*/
