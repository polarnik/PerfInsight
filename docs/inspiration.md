# Project inspiration

## Conversations with Leonid Striuk, Team Lead, YouTrack – Backend, JetBrains Germany - Munich

> I would like to see a thread dump and stack traces in a performance issue, not just the name of the slow HTTP method.

> Well-documented performance issues are resolved faster, while issues with insufficient description may remain unaddressed for a long time.

#### 🌟 Performance Insight

We can prepare a detailed description of the performance issue with the stack traces and thread dumps:

- YouTrack provides API for the issue creation
- YouTrack provides API for the article creation
- YouTrack provides Hooks for interactions like Workflow Scripts

It will enable the human in the loop process for the performance issues.

## Conversation with Maxim Chechulnikov, Software Developer, GoLand, JetBrains Germany - Munich

> It would be great to identify the commit that introduced the performance degradation based on profiling data, without the need to perform a binary search or run benchmarks multiple times to compare their results.

#### 🌟 Performance Insight

We can align performance continuous profiling with the code changes and identify the commit that introduced the performance degradation:

- Grafana Pyroscope provides API for the profiling data
- TeamCity provides API for the build results with the version number and commits
- Git provides API for getting filenames by commits


## The Pragmatic Engineer: Measuring the impact of AI on software engineering – with Laura Tacho, Jul 23, 2025
Source: https://podcasts.apple.com/cy/podcast/the-pragmatic-engineer/id1769051199?i=1000718710555&r=35
This material may be protected by copyright.

### A description of the Podcast issue about the time savings of a developer

> “Today we discuss why most of the hype in the media about AI gets things wrong thanks to oversimplification, and why the burden is on us engineers to set the record straight. The actual data of the impact of rolling out AI tools for development at companies booking.com and WorkHuman, how developers report that their most timing-saving use case is not actually AI code generation, but **_debugging tricky stack traces and doing it faster_**. The paradox of AI tools, how using AI tools to help with coding can make developers less satisfied with our jobs because we actually like to code.”

#### 🌟 Performance Insight

Debugging tricky stack traces is a main use case of AI tools for time savings.
Performance traces have gigabytes of tricky stack traces. Developers don't like to read and compare stack traces. The AI tools can help developers to find the most time-consuming stack traces and analyze them.

### TOP-3 time savings of a developer

> “Yeah. I want to show this to you because I think that exactly that point that you made, that maybe the biggest gain is not in code generation, but in like, you could still use it for brainstorming, you can still use it for **error analysis**. This is part of that enablement in training that companies can offer in order to increase adoption, increase impact.
>
> We did a study of 180 plus companies and we looked at the developers who were saving a serious amount of time with AI, and we tried to understand like, what are you actually doing? And interestingly enough, code generation, like **mid-loop code generation**, is the third highest use case for saving time. But actually, **stack trace analysis** and **refactoring existing code** were saving more time than the mid-loop code generation.”

#### 🌟 Performance Insight

Debugging tricky stack traces is a main use case of AI tools for time savings.
Performance traces have gigabytes of tricky stack traces. Developers don't like to read and compare stack traces. The AI tools can help developers to find the most time-consuming stack traces and analyze them.

### A story about a good day from On The Pragmatic Engineer

> I think you, you know, when I was thinking, when I was a developer and then was a manager with developers, like, what is a good day as a developer? And I think, you know, an average good day, like, again, there can be different days, but it was like, I come in to work, you know, we say hi to people, you know, maybe we talk about something. Usually it's, we don't have meetings.
>
> **I have a clear goal in mind**. It's something challenging that I want to complete. Something maybe it might have been from yesterday, or I'm now just starting as fresh.
>
> **I get into the zone**. I, you know, get it together. **I get it working.**
>
> **I clean it up. It's working**. It's amazing.
>
> **I commit it** or **I test it**. **I check it**. I put up a pull request and **I'm done**.
>
> And, you know, if this happens at 2 p.m. And it was something challenging. **It should have taken me eight hours, but I got it done in four and I'm really proud of it.** It works.
>
> It does. Maybe I could clean up. And then I help out some people.


#### 🌟 Performance Insight

Developers don't like interruptions and distractions. Will be nice to define and support:

- Setting up a clear goal about performance improvements
- Preparing code diff suggestions for performance improvements
- Testing the performance improvement
  and
- Getting into the zone


### A conversation about the measurement of AI

> “And just so I understand, I think for anyone like saying, so they were measuring these things before, right? And then as they rolled it out, they kept measuring it, and now they're seeing a gain, right? Because you can only get an improvement on something that you measure.
>
> So if I'm working at a company which never measured anything, I can measure now and have a baseline, but unless I did it before AI, it's going to be a bit harder for me to tell how much has it improved, because I don't have data before.”



> “Yeah, exactly. And so my other general advice is start measuring now. We can pull in historical data to cover some gaps.
>
> We can look at GitHub and Jira and those tools historically. When it comes to surveying on the developer experience and from those self-reported things, just start small. Just get started.”

#### 🌟 Performance Insight

We can get and analyze historical data about performance problems from YouTrack and identify the bottlenecks via the issue states. Then we can track the process improvements with the new tool **Performance Insights**.
